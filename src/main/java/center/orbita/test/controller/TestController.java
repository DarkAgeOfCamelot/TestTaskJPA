package center.orbita.test.controller;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import center.orbita.test.counterdb.ReadCounter;
import center.orbita.test.model.TestCounter;

@RestController
public class TestController {

    private final AtomicLong counter = new AtomicLong();
    @Autowired
    ReadCounter readCouner;
    
    @GetMapping("/")
    public String welcome() {
        return "Welcome to RestTemplate Example.";
    }
 
    @GetMapping("/testrest")
    public TestCounter getTestRest() {
    	return new TestCounter(readCouner.getMaxCounter() );
    }
    
    @DeleteMapping("/testrest")
    public TestCounter deleteTestRest() {
    	return new TestCounter(readCouner.zeroingCounterAndGet() );
    }
    
    
    @PostMapping("/testrest")
    public TestCounter postTestRest() {
    	return new TestCounter(readCouner.incrementCounterAndGet() );
    }
}
