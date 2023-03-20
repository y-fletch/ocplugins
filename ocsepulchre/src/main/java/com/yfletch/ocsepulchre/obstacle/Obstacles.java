package com.yfletch.ocsepulchre.obstacle;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.obstacle.floor1.F1EKnight;
import com.yfletch.ocsepulchre.obstacle.floor1.F1EWizard1;
import com.yfletch.ocsepulchre.obstacle.floor1.F1EWizard2;
import com.yfletch.ocsepulchre.obstacle.floor1.F1SKnight;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.experimental.PackagePrivate;

@Getter
@Singleton
public class Obstacles
{
	@PackagePrivate
	@Inject OCSepulchreContext context;

	@Inject F1SKnight f1SKnight;

	@Inject F1EWizard1 f1EWizard1;
	@Inject F1EKnight f1EKnight;
	@Inject F1EWizard2 f1EWizard2;

	public Set<Obstacle> all()
	{
		return Set.of(
			f1SKnight,
			f1EWizard1,
			f1EKnight,
			f1EWizard2
		);
	}

	public Set<DrawableObstacle> allDrawable()
	{
		return all().stream()
			.filter(o -> o instanceof DrawableObstacle)
			.map(o -> (DrawableObstacle) o)
			.collect(Collectors.toSet());
	}

	public void tick()
	{
		for (Obstacle obstacle : all())
		{
			if (obstacle.tickIf(context))
			{
				obstacle.tick(context);
			}
		}
	}
}
