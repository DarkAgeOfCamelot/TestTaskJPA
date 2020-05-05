package center.orbita.test.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class TestCounter {
	@Getter
	@Setter
	private long id;
	
    public TestCounter(long id) {
        this.id = id;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
}
