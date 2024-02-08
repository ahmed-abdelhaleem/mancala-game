package com.bol.mancala.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.bol.mancala.model.ActionResponse;
import com.bol.mancala.model.BoardLinkedList;
import com.bol.mancala.model.PitNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Service
public class MancalaService {

	private final String pitPrefex = "pit_";

	private Cache<String, BoardLinkedList> mutlipleBoardCachedLinkedList = CacheBuilder.newBuilder().maximumSize(1000)
			.expireAfterWrite(60, TimeUnit.MINUTES).build();

	public ActionResponse getActionResponse(String clientId, Integer pitNumber) throws ExecutionException {

		return getAllPitsWithNewValues(mutlipleBoardCachedLinkedList.getIfPresent(clientId), pitNumber, clientId);
	}

	private ActionResponse getAllPitsWithNewValues(BoardLinkedList boardLinkedList, Integer pitPosition,
			String clientId) {

		ActionResponse actionResponse = new ActionResponse();

		Map<String, Integer> pitMap = new HashMap<>();

		if (boardLinkedList == null) {
			boardLinkedList = getInitialPitsLinkedList();
		}

		PitNode targetPit = boardLinkedList.getPitByPosition(pitPosition);

		Integer pitStonesNumber = targetPit.getStonesNumber();

		targetPit.setStonesNumber(0);

		while (pitStonesNumber > 0) {

			targetPit = targetPit.getNextPitNode();
			int targetPitLocation = targetPit.getPitPosition();
			if (pitPosition < boardLinkedList.size() / 2) {

				if (targetPitLocation != boardLinkedList.size()) {
					int oldStonesNumber = targetPit.getStonesNumber();

					targetPit.setStonesNumber(oldStonesNumber + 1);
					pitMap.put(pitPrefex + (targetPit.getPitPosition()), oldStonesNumber + 1);
					pitStonesNumber--;

					if (pitStonesNumber == 0) {

						if (targetPit.getPitPosition() == boardLinkedList.size() / 2) {
							actionResponse.setNextPlayClient(1);
						} else {
							actionResponse.setNextPlayClient(2);
							if (targetPit.getStonesNumber() == 1 && targetPit.getPitPosition()<boardLinkedList.size()/2) {
								setTotalNewPlayerOneStoreStones(boardLinkedList,pitMap, targetPit.getPitPosition());
							}
						}
					}

				}
			} else {

				if (targetPitLocation != boardLinkedList.size() / 2) {
					int oldStonesNumber = targetPit.getStonesNumber();

					targetPit.setStonesNumber(oldStonesNumber + 1);
					pitMap.put(pitPrefex + (targetPit.getPitPosition()), oldStonesNumber + 1);
					pitStonesNumber--;

					if (pitStonesNumber == 0) {

						if (targetPit.getPitPosition() == boardLinkedList.size()) {
							actionResponse.setNextPlayClient(2);
						} else {
							actionResponse.setNextPlayClient(1);
							if (targetPit.getStonesNumber() == 1 && targetPit.getPitPosition()>boardLinkedList.size()/2) {
								setTotalNewPlayerOneStoreStones(boardLinkedList,pitMap, targetPit.getPitPosition());
							}
						}
					}
				}
			}
		}

		pitMap.put(pitPrefex + (pitPosition), 0);
		actionResponse.setPitsStoneNumber(pitMap);

		if (gameOver(boardLinkedList)) {
			if (boardLinkedList.getPlayerOneTotalStones() > boardLinkedList.getPlayerTwoTotalStones()) {
				actionResponse.setWinner(1);
			} else {
				actionResponse.setWinner(2);
			}
		}

		mutlipleBoardCachedLinkedList.put(clientId, boardLinkedList);

		return actionResponse;

	}

	private void setTotalNewPlayerOneStoreStones(BoardLinkedList boardLinkedList,Map<String, Integer> pitMap, int pitPosition) {

		PitNode activeplayerPitNode = boardLinkedList.getPitByPosition(pitPosition);

		int tobeSubtractedFromSize = activeplayerPitNode.getPitPosition() - 1;

		int positionOfEliminatedStonesPit = boardLinkedList.size() - tobeSubtractedFromSize;

		PitNode otherPlayerPitNode = boardLinkedList.getPitByPosition(positionOfEliminatedStonesPit);

		PitNode activePlayStorePit = null;
		if (pitPosition < boardLinkedList.size() / 2) {
			activePlayStorePit = boardLinkedList.getPitByPosition(boardLinkedList.size() / 2);
		} else {
			activePlayStorePit = boardLinkedList.getPitByPosition(boardLinkedList.size());
		}

		activePlayStorePit.setStonesNumber(activePlayStorePit.getStonesNumber() + otherPlayerPitNode.getStonesNumber()
				+ activeplayerPitNode.getStonesNumber());
		pitMap.put(pitPrefex + activePlayStorePit.getPitPosition(), activePlayStorePit.getStonesNumber());

		activeplayerPitNode.setStonesNumber(0);
		pitMap.put(pitPrefex + activeplayerPitNode.getPitPosition(), 0);
		
		otherPlayerPitNode.setStonesNumber(0);
		pitMap.put(pitPrefex + otherPlayerPitNode.getPitPosition(), 0);
	}

	private boolean gameOver(BoardLinkedList boardLinkedList) {

		if (boardLinkedList.noStonesLeftForPlayerOne() || boardLinkedList.noStonesLeftForPlayerTwo()) {
			return true;
		}
		return false;
	}

	private BoardLinkedList getInitialPitsLinkedList() {
		BoardLinkedList pitsLinkedList = new BoardLinkedList();

		for (int i = 1; i <= 14; i++) {
			if (i == 7 || i == 14) {
				pitsLinkedList.insertAtTail(i, 0);
			} else {
				pitsLinkedList.insertAtTail(i, 6);
			}
		}
		return pitsLinkedList;
	}
}