/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.universal.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.luxvacuos.voxel.universal.resources.IDisposable;

public abstract class ObjectPool<T extends IPoolable> implements IDisposable {
	
	private ConcurrentLinkedQueue<T> pool;

    private ScheduledExecutorService executor;
    
    public ObjectPool(final int minIdle, final int maxIdle) {
    	this(minIdle, maxIdle, 60);
    }

	public ObjectPool(final int minIdle, final int maxIdle, final long validationInterval) {
        // initialize pool
        this.initialize(minIdle);

        // check pool conditions in a separate thread
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.executor.scheduleWithFixedDelay(new Runnable()
        {
            @Override
            public void run() {
                int size = ObjectPool.this.pool.size();
                if (size < minIdle) {
                    int sizeToBeAdded = minIdle - size;
                    for (int i = 0; i < sizeToBeAdded; i++) {
                    	ObjectPool.this.pool.add(create());
                    }
                } else if (size > maxIdle) {
                    int sizeToBeRemoved = size - maxIdle;
                    for (int i = 0; i < sizeToBeRemoved; i++) {
                    	ObjectPool.this.pool.poll();
                    }
                }
            }
        }, validationInterval, validationInterval, TimeUnit.SECONDS);
    }
	
	public T borrow() {
        T object;
        if ((object = this.pool.poll()) == null) {
            object = create();
        }

        return object;
    }
	
	public void release(T object) {
        if (object == null) {
            return;
        }
        
        object.reset();
        this.pool.offer(object);
    }
	
	protected abstract T create();
	
	@Override
	public void dispose() {
		this.executor.shutdown();
	}
	
	private void initialize(final int minIdle) {
        this.pool = new ConcurrentLinkedQueue<T>();

        for (int i = 0; i < minIdle; i++) {
            this.pool.add(create());
        }
    }

}