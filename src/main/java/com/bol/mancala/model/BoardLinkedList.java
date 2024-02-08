package com.bol.mancala.model;

public class BoardLinkedList {

	private PitNode head;

	private int size = 0;

	public void insertAtBeginning(int nodeLocation, int value) {
		PitNode newNode = new PitNode(nodeLocation, value, null);
		if (head == null) {
			head = newNode;
			head.setNextPitNode(head);
		} else {
			PitNode temp = head;
			newNode.setNextPitNode(temp);
			head = newNode;
		}
		size++;
	}

	public void insertAtTail(int nodeLocation, int value) {
		PitNode newNode = new PitNode(nodeLocation, value, null);
		if (null == head) {
			head = newNode;
		} else {
			PitNode temp = head;
			while (temp.getNextPitNode() != head) {
				temp = temp.getNextPitNode();
			}
			temp.setNextPitNode(newNode);
		}
		newNode.setNextPitNode(head);
		size++;
	}

	public void insertAtPosition(int position, int value) {
		if (position < 0 || position > size) {
			throw new IllegalArgumentException("Position is Invalid");
		}

		PitNode newNode = new PitNode(position, value, null);
		PitNode tempNode = head;
		PitNode prevNode = null;
		for (int i = 0; i < position; i++) {
			if (tempNode.getNextPitNode() == head) {
				break;
			}
			prevNode = tempNode;
			tempNode = tempNode.getNextPitNode();
		}
		prevNode.setNextPitNode(newNode);
		newNode.setNextPitNode(tempNode);
		size++;
	}

	public void deleteFromBeginning() {
		PitNode temp = head;
		while (temp.getNextPitNode() != head) {
			temp = temp.getNextPitNode();
		}
		temp.setNextPitNode(head.getNextPitNode());
		head = head.getNextPitNode();
		size--;
	}

	public void deleteFromPosition(int position) {
		if (position < 0 || position >= size) {
			throw new IllegalArgumentException("Position is Invalid");
		}
		PitNode current = head, previous = head;
		for (int i = 0; i < position; i++) {
			if (current.getNextPitNode() == head) {
				break;
			}
			previous = current;
			current = current.getNextPitNode();
		}
		if (position == 0) {
			deleteFromBeginning();
		} else {
			previous.setNextPitNode(current.getNextPitNode());
		}
		size--;
	}

	public PitNode getPitByPosition(int position) {
		if (position < 1 || position > size) {
			throw new IndexOutOfBoundsException("Index is Invalid");
		}
		PitNode temp = head;
		for (int i = 1; i <position; i++) {
			temp = temp.getNextPitNode();
		}
		return temp;
	}

	public boolean noStonesLeftForPlayerOne() {
		if (size < 1) {
			throw new IndexOutOfBoundsException("Index is Invalid");
		}
		PitNode temp = head;
		for (int i = 1; i < size / 2; i++) {
			if (temp.getStonesNumber() > 0) {
				return false;
			} else {
				temp = temp.getNextPitNode();
			}
		}
		return true;
	}

	public boolean noStonesLeftForPlayerTwo() {
		if (size < 1) {
			throw new IndexOutOfBoundsException("Index is Invalid");
		}
		PitNode temp = head;
		for (int i = size / 2 + 1; i < size; i++) {
			if (temp.getStonesNumber() > 0) {
				return false;
			} else {
				temp = temp.getNextPitNode();
			}
		}
		return true;
	}

	public Integer getPlayerOneTotalStones() {
		Integer total = 0;
		if (size < 1) {
			throw new IndexOutOfBoundsException("Index is Invalid");
		}
		PitNode temp = head;
		for (int i = 1; i <= size / 2; i++) {
			total += temp.getStonesNumber();
			temp = temp.getNextPitNode();
		}

		return total;
	}

	public Integer getPlayerTwoTotalStones() {
		Integer total = 0;
		if (size < 1) {
			throw new IndexOutOfBoundsException("Index is Invalid");
		}
		PitNode temp = head;
		for (int i = size / 2 + 1; i <= size; i++) {
			total += temp.getStonesNumber();
			temp = temp.getNextPitNode();
		}

		return total;

	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}
}