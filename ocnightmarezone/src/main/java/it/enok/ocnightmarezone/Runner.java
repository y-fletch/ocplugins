package it.enok.ocnightmarezone;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;

@Singleton
public class Runner extends ActionRunner<Context>
{
	@Inject
	public Runner(Context context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);
	}

	@Override
	public void setup(Context context)
	{
//		add(builder().prep().withConditions());

		add(builder().consume("Wait").readyIf(ctx -> true));
	}
}
