package voxel.server.core.engine.model;

public abstract class Entity {
	protected Position position = new Position(0, 0, 0);
	protected boolean _isPlayer;

	public Position getPosition() {
		return position;
	}

	public void setisPlayer(boolean value) {
		_isPlayer = value;
	}

	public boolean isPlayer() {
		return _isPlayer;
	}
}
