package cubegame.save;

import java.io.File;

public class CubeMapFile {
	File file;
	
	public CubeMapFile(File file){
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
