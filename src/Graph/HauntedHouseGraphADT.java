package Graph;

import Exceptions.VertexNotFoundException;

public interface HauntedHouseGraphADT<T> {
    
    public boolean isComplete();
    public boolean isAlive();
    public boolean changePosition(T nextPosition) throws VertexNotFoundException;
    public T getCurrentPosition();

}
