package neil.demo.flake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Return an identifier using an clustered generator.
 * These are named, you can have an many independent generators
 * are you like. Each generator has an instance on each process,
 * allocated a block of numbers.
 * </p>
 * <p>
 * <p>The numbers allocated are not sequential. The process
 * you are using gives you numbers from a block of numbers, then
 * fetches another block. This may not be the next in series if
 * other processes have fetched blocks
 * of numbers also.
 * </p>
 * <p>The number generated is unique, even for very high
 * generation rates. This is unlike implementations that
 * rely on the system clock granularity.
 * </p>
 * <p>The numbers are based around timestamps, but are unique
 * even if the system clock does not tick forward fast enough.
 * </p>
 * <p>As each process is releasing numbers from a block, this
 * is scalable. Ten processes can issue unique identifiers at
 * twice the rate of five processes.
 * </p>
 */

@RestController
@Slf4j
public class FlakeController {

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@GetMapping("flakeidgenerator")
	public Long next() {
		log.info("next()");
		
		FlakeIdGenerator counter2 = 
				this.hazelcastInstance.getFlakeIdGenerator("counter2");
		
		return counter2.newId();
	}
	
}
