package org.tilegames.hexicube.worms.network;

public class Message {
	
	public static final int
		MAP_INFO = 1,
		WORM_NAMES = 2,
		KEY_STATES = 3;
	
	public int type;
	public int sender; // todo: determine how's best to set this?
	
	public static Message create(String line) {
		String[] data = line.split(",");
		int type = Integer.valueOf(data[0]);
		switch (type) {
			case MAP_INFO: return new MapInfo(data);
			case WORM_NAMES: return new WormNames(data);
			case KEY_STATES: return new KeyStates(data);
		}
		return null;
	}
	
	public static class MapInfo extends Message {
		public MapInfo(String[] data) {
			
		}
	}

	public static class WormNames extends Message {
		public WormNames(String[] data) {

		}
	}

	public static class KeyStates extends Message {
		public KeyStates(String[] data) {

		}
	}
}
