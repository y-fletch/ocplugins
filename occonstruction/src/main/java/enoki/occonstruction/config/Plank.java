package enoki.occonstruction.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

@Getter
@RequiredArgsConstructor
public enum Plank
{
	OAK("Oak", ItemID.OAK_PLANK, ItemID.OAK_PLANK + 1);
    //	MAHOGANY("Mahogany", ItemID.MAHOGANY_PLANK, ItemID.MAHOGANY_PLANK + 1);

	private final String label;
	private final int itemId;
	private final int notedId;

	@Override
	public String toString()
	{
		return label;
	}
}
