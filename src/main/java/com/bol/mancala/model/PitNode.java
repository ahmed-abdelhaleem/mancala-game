package com.bol.mancala.model;

public class PitNode {

	private int stonesNumber;
	private int pitPosition;
	private PitNode next;

	public PitNode(int pitPosition,int initialStoneNumber, PitNode nextNode) {
		this.pitPosition = pitPosition;
		this.stonesNumber = initialStoneNumber;
		next = nextNode;
	}

	public void setNextPitNode(PitNode n) {
		next = n;
	}

	public PitNode getNextPitNode() {
		return next;
	}

	public void setStonesNumber(int d) {
		stonesNumber = d;
	}

	public int getStonesNumber() {
		return stonesNumber;
	}

	public int getPitPosition() {
		return pitPosition;
	}
}