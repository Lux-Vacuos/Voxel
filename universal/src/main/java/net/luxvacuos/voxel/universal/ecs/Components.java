package net.luxvacuos.voxel.universal.ecs;

import com.badlogic.ashley.core.ComponentMapper;

import net.luxvacuos.voxel.universal.ecs.components.*;

public class Components {

	protected Components() {
		throw new RuntimeException("Cannot Initialize the Components class!");
	}
	
	public static final ComponentMapper<Position> POSITION = ComponentMapper.getFor(Position.class);
	
	public static final ComponentMapper<Rotation> ROTATION = ComponentMapper.getFor(Rotation.class);
	
	public static final ComponentMapper<Scale> SCALE = ComponentMapper.getFor(Scale.class);
	
	public static final ComponentMapper<Velocity> VELOCITY = ComponentMapper.getFor(Velocity.class);
	
	public static final ComponentMapper<Health> HEALTH = ComponentMapper.getFor(Health.class);
}
