package io.github.guerra24.voxel.client.kernel.sound.soundsystem;

public interface IStreamListener {
	/**
	 * Notifies implementation that an End Of Stream was reached.
	 * 
	 * @param sourcename
	 *            String identifier of the source which reached the EOS.
	 * @param queueSize
	 *            Number of items left the the stream's play queue, or zero if
	 *            none.
	 */
	public void endOfStream(String sourcename, int queueSize);
}
