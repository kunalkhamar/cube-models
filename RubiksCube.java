class RubiksCube {
	Block[][][] blocks;

	public enum Color { NONE, WHITE, YELLOW, BLUE, GREEN, RED, ORANGE }
	public enum Face { XY, YZ, XZ }

	public RubiksCube(Block[][][] cube) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 3; k++)
					blocks[i][j][k] = cube[i][j][k];
	}

	/**
	 * Rotate a face cw
	 * cmd is one of:
	 * [ 'R' (right), 'L' (left), 'F' (front), 'B' (back), 
	 *   'D' (down) , 'U' (up) ]
	 */
	public void rotatecw(char cmd) {
		Block[][] face;
		switch (cmd) {
			case 'R':
				face = extractFace(-1, 2, -1);
				rotatecw(face);
				flipAll(face, Face.YZ);
				insertFace(face, -1, 2, -1);
			case 'L':
				face = extractFace(-1, 0, -1);
				rotatecw(face);
				flipAll(face, Face.YZ);
				insertFace(face, -1, 0, -1);
			case 'F':
				face = extractFace(-1, -1, 0);
				rotatecw(face);
				flipAll(face, Face.XY);
				insertFace(face, -1, -1, 0);
			case 'B':
				face = extractFace(-1, -1, 2);
				rotatecw(face);
				flipAll(face, Face.XY);
				insertFace(face, -1, -1, 2);
			case 'D':
				face = extractFace(2, -1, -1);
				rotatecw(face);
				flipAll(face, Face.XZ);
				insertFace(face, 2, -1, -1);
			case 'U':
				face = extractFace(0, -1, -1);
				rotatecw(face);
				flipAll(face, Face.XZ);
				insertFace(face, 0, -1, -1);
		}
	}

	private Block[][] extractFace(int x, int y, int z) {
		Block[][] res = new Block[3][3];
		if (x != -1) {
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 3; k++)
					res[j][k] = blocks[x][j][k];
		} else if (y != -1) {
			for (int i = 0; i < 3; i++)
				for (int k = 0; k < 3; k++)
					res[i][k] = blocks[i][y][k];
		} else if (z != -1) {
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					res[i][j] = blocks[i][j][z];
		}
		return res;
	}

	private void insertFace(Block[][] face, int x, int y, int z) {
		if (x != -1) {
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 3; k++)
					blocks[x][j][k] = face[j][k];
		} else if (y != -1) {
			for (int i = 0; i < 3; i++)
				for (int k = 0; k < 3; k++)
					blocks[i][y][k] = face[i][k];
		} else if (z != -1) {
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					blocks[i][j][z] = face[i][j];
		}
	}

	private static void flipAll(Block[][] face, Face unchanged) {
		for (int i = 0; i < face.length; i++) {
			for (int j = 0; j < face[i].length; j++) {
				face[i][j].flip(unchanged);
			}
		}
	}

	private static void rotatecw(Block[][] m) {
		// A cw cube rotation is a combination of two operations
		reverseRows(m);
		transpose(m);
	}

	private static void reverseRows(Block[][] m) {
		int exch = m.length / 2;
		int rLast = m.length - 1;
		for (int i = 0; i < exch; i++) {
			Block[] temp = m[i];
			m[i] = m[rLast - i];
			m[rLast - i] = temp;
		}
	}

	private static void transpose(Object[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (i == j) continue;
				Object temp = m[i][j];
				m[i][j] = m[j][i];
				m[j][i] = temp;
			}
		}
	}
	
	static class Block {
		Color i, j, k; // color of face perpendicular to vector
		
		enum Type {
			INNER, CENTER, EDGE, CORNER
		}
		Type type;

		public Block(Type t, Color i, Color j, Color k) {
			type = t;
			this.i = i;
			this.j = j;
			this.k = k;
		}

		public void flip(Face unchanged) {
			if (type == Type.CENTER)
				return;
			
			Color temp;
			switch (unchanged) {
				case XY:
					temp = i;
					i = j;
					j = temp;
					break;
				case YZ:
					temp = j;
					j = k;
					k = temp;
					break;
				case XZ:
					temp = i;
					i = k;
					k = temp;
					break;
			}
		}
	}
}
