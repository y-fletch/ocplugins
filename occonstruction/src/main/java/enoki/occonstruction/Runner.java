package enoki.occonstruction;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import net.runelite.api.Client;
import net.unethicalite.client.Static;

@Singleton
public class Runner extends ActionRunner<Context>
{
	@Inject private Client client;

	@Inject
	public Runner(Context context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);
	}

	@Override
	public void setup(Context context)
	{
		// can only get item compositions on client thread
		if (!Static.getClient().isClientThread())
		{
			return;
		}

		add(builder().consume("Nothing").readyIf(ctx -> true));
	}
}
