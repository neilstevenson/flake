package neil.demo.flake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Return an identifier using an clustered atomic long.
 * These are named, you can have as many indpendedent counters
 * as you like. Each counter exists as a single object.
 * </p>
 * <p>This is sequential, values returned go 1, 2, 3, 4, etc.
 * </p>
 * <p>This is not guaranteed to be unique, as you can reset
 * the counter value back to the beginning.
 * </p>
 * <p>The counter has to be a singleton, so can be a bottlneck
 * if numbers need generated at a very high rate. It is hosted
 * on one server, if you add five, ten or twenty more servers
 * it doesn't help, there is one counter that all increment
 * operations interact with.
 * </p>
 */
@RestController
@Slf4j
public class SequenceController {

	@Autowired
	private HazelcastInstance hazelcastInstance;
	
	@GetMapping("iatomiclong")
	public Long next() {
		log.info("next()");
		
		IAtomicLong counter1 =
				this.hazelcastInstance.getAtomicLong("counter1");
		
		return counter1.incrementAndGet();
	}

}
