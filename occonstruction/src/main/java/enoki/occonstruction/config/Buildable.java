package enoki.occonstruction.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ObjectID;

/*
 * Notes:
 * Larder Space = 15403
 * Oak Larder = 13566
 */

@Getter
@RequiredArgsConstructor
public enum Buildable
{
	LARDER("Larder", ObjectID.LARDER_13566, ObjectID.LARDER_SPACE);

	private final String label;
	private final int objectId;
	private final int spaceId;

	@Override
	public String toString()
	{
		return label;
	}
}
