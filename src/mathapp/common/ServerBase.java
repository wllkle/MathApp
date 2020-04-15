package mathapp.common;

// This interface is being used to ensure all three server types (iterative, concurrent and HTTP)
// can be treated equally by mathapp.Server

public interface ServerBase {
    void start();
}
