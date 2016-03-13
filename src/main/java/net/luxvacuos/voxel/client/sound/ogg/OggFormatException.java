/*
 * $ProjectName$
 * $ProjectRevision$
 * -----------------------------------------------------------
 * $Id: OggFormatException.java,v 1.1 2003/03/03 21:02:20 jarnbjo Exp $
 * -----------------------------------------------------------
 *
 * $Author: jarnbjo $
 *
 * Description:
 *
 * Copyright 2002-2003 Tor-Einar Jarnbjo
 * -----------------------------------------------------------
 *
 * Change History
 * -----------------------------------------------------------
 * $Log: OggFormatException.java,v $
 * Revision 1.1  2003/03/03 21:02:20  jarnbjo
 * no message
 *
 */

package net.luxvacuos.voxel.client.sound.ogg;

import java.io.IOException;

/**
 * Exception thrown when trying to read a corrupted Ogg stream.
 */

public class OggFormatException extends IOException {

	private static final long serialVersionUID = 3929282865396925678L;

	public OggFormatException() {
		super();
	}

	public OggFormatException(String message) {
		super(message);
	}
}