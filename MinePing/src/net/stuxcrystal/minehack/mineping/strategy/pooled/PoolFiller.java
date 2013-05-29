package net.stuxcrystal.minehack.mineping.strategy.pooled;

import java.net.InetSocketAddress;

import net.stuxcrystal.minehack.mineping.MinePing;
import net.stuxcrystal.minehack.mineping.MinePingInstances;
import net.stuxcrystal.minehack.mineping.api.AbstractPingEventHandler;

public class PoolFiller extends AbstractPingEventHandler {

	PoolStrategy strategy;

	MinePing minePing;

	public PoolFiller(PoolStrategy strategy) {
		this.strategy = strategy;
		this.minePing = MinePingInstances.getMinePing();
	}

	public void tick() {
		InetSocketAddress nextAddress;
		while (strategy.running.size() <= strategy.maxQueue) {
			nextAddress = minePing.getNextAddress();
			if (nextAddress == null) {
				strategy.gracefulInterrupt();
				break;
			}

			PingRunnable pr = new PingRunnable(nextAddress, minePing);
			strategy.executor.execute(pr);
			strategy.running.add(pr);
		}
	}

}
