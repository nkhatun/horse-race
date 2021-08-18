package nkhatun.demo.harrykart.java.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import nkhatun.demo.harrykart.java.dto.LaneDto;
import nkhatun.demo.harrykart.java.dto.LoopDto;
import nkhatun.demo.harrykart.java.dto.ParticipantDto;
import nkhatun.demo.harrykart.java.dto.RankingDto;
import nkhatun.demo.harrykart.java.dto.RankingHolder;
import nkhatun.demo.harrykart.java.dto.RequestDto;
@Service
public class HarryKartServiceImpl implements HarryKartService {
	Logger logger = LoggerFactory
			.getLogger(HarryKartServiceImpl.class.getName());
	private static final BigInteger TRACK_LENGTH = BigInteger.valueOf(1000);
	public ResponseEntity<RankingHolder> applyPower(RequestDto requestDto) {
		Map<BigInteger, BigInteger> laneTimeMap = new LinkedHashMap<BigInteger, BigInteger>();
		List<ParticipantDto> participantDtoList = requestDto.getStartList();

		if (requestDto.getNumberOfLoops() == BigInteger.valueOf(0)
				|| participantDtoList.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		if (requestDto.getNumberOfLoops() == BigInteger.valueOf(1)
				&& participantDtoList.size() > 1) {
			// no power ups applied, ranking from the base speed
			requestDto.getStartList().stream().forEach(participant -> {
				Long calculatedTime = calculateTime(
						participant.getBaseSpeed().longValue());
				if (calculatedTime != 0) {
					laneTimeMap.put(participant.getLane(),
							BigInteger.valueOf(calculatedTime));
				} else {
					laneTimeMap.put(participant.getLane(), null);
				}
			});
		} else {
			List<LoopDto> loopDtoList = requestDto.getPowerUps();
			if ((loopDtoList
					.size()) != (requestDto.getNumberOfLoops().longValue()
							- 1)) {
				return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
			}

			// Created lane map with speed list
			Map<BigInteger, List<BigInteger>> laneSpeedMap = new LinkedHashMap<BigInteger, List<BigInteger>>();

			loopDtoList.stream().forEach(loop -> {
				List<LaneDto> laneDtoList = loop.getLane();
				laneDtoList.stream().forEach(lane -> {
					BigInteger BASE_SPEED = getBaseSpeedPerLane(
							lane.getNumber(), participantDtoList);
					if (BASE_SPEED.longValue() > 0) {
						if (laneSpeedMap.containsKey(lane.getNumber())
								&& laneSpeedMap.get(lane.getNumber()) == null) {
							logger.info(
									"Base speed is less than zero in previous loop::  "
											+ BASE_SPEED + " For lane no: "
											+ lane.getNumber());
							laneSpeedMap.put(lane.getNumber(), null);
						} else if (laneSpeedMap.containsKey(lane.getNumber())
								&& laneSpeedMap.get(lane.getNumber()) != null) {
							List<BigInteger> speedList = laneSpeedMap
									.get(lane.getNumber());
							BigInteger currentSpeed = speedList
									.get(speedList.size() - 1)
									.add(lane.getValue());
							speedList.add(currentSpeed);
							laneSpeedMap.put(lane.getNumber(), speedList);
						} else {
							List<BigInteger> speedList = new ArrayList<>();
							BigInteger currentSpeed = BASE_SPEED
									.add(lane.getValue());
							speedList.add(currentSpeed);
							laneSpeedMap.put(lane.getNumber(), speedList);
						}
					} else {
						logger.info(
								"Base speed is less than zero:: set for out from race "
										+ BASE_SPEED + " For lane no: "
										+ lane.getNumber() + " Loop no: "
										+ loop.getNumber());
						laneSpeedMap.put(lane.getNumber(), null);
					}

				});
			});

			// calculate time based on the lane speed list
			laneSpeedMap.entrySet().stream().forEach(lane -> {
				List<BigInteger> speedList = lane.getValue();
				AtomicLong laneTime = new AtomicLong(0);
				if (speedList != null) {
					speedList.stream().filter(speed -> speed != null)
							.forEach(speed -> {
								long calculatedTime = calculateTime(
										speed.doubleValue());
								if (calculatedTime != 0) {
									laneTime.addAndGet(
											calculateTime(speed.doubleValue()));
								} else {
									laneTime.set(calculatedTime);
									return;
								}
							});
				}
				laneTimeMap.put(lane.getKey(),
						(laneTime.get() != BigInteger.ZERO.longValue())
								? BigInteger.valueOf(laneTime.get())
								: null);
			});
			logger.info("Lane time map for each participant exists in race::  "
					+ laneTimeMap);
		}
		RankingHolder rankingHolder = prepareRankingDtoList(laneTimeMap,
				participantDtoList);
		return new ResponseEntity<RankingHolder>(rankingHolder, HttpStatus.OK);
	}

	private RankingHolder prepareRankingDtoList(
			Map<BigInteger, BigInteger> laneTimeMap,
			List<ParticipantDto> participantDtoList) {
		// sort laneTimeMap based on time ascending and limit to 3
		AtomicInteger ordinal = new AtomicInteger(1);
		List<RankingDto> rankingDtoList = new ArrayList<RankingDto>();
		laneTimeMap.entrySet().stream()
				.filter(timeMap -> timeMap.getValue() != null)
				.sorted(Map.Entry.<BigInteger, BigInteger>comparingByValue())
				.limit(3L).forEachOrdered(lane -> {
					participantDtoList.stream()
							.filter(participant -> lane.getKey()
									.equals(participant.getLane()))
							.forEach(p -> {
								RankingDto rankingDto = new RankingDto();
								rankingDto.setPosition(Long
										.valueOf(ordinal.getAndIncrement()));
								rankingDto.setHorse(p.getName());
								rankingDtoList.add(rankingDto);
							});
				});

		logger.info("preapred ranking dto list :: " + rankingDtoList);
		RankingHolder rankingHolder = new RankingHolder(rankingDtoList);
		return rankingHolder;
	}

	public long calculateTime(double currentSpeed) {
		try {
			if (currentSpeed > 0) {
				double time = ((TRACK_LENGTH.doubleValue() / currentSpeed));
				return (long) time;
			} else {
				return 0;
			}
		} catch (ArithmeticException ex) {
			logger.info("Got an exception while calculating time : "
					+ ex.getMessage());
			return 0;
		}
	}

	public BigInteger getBaseSpeedPerLane(BigInteger lane,
			List<ParticipantDto> participantDtoList) {
		return participantDtoList.stream().filter(p -> p.getLane().equals(lane))
				.map(map -> map.getBaseSpeed()).collect(Collectors.toList())
				.get(0);
	}
}
