package cubegame.save;

import java.io.FileNotFoundException;
import java.io.IOException;

import cubegame.map.CubeMap;
import cubegame.multiplayer.HumanMPPacket;

public class LevelSaver {
	public static void saveLevel(String levelname, HumanMPPacket p, CubeMap map) throws FileNotFoundException, IOException{
		SGSHelper sgs = new SGSHelper("saves/level_"+levelname+"/localinfo.sgs");
	}
}
