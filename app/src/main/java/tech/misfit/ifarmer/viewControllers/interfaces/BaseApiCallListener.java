package tech.misfit.ifarmer.viewControllers.interfaces;

public interface BaseApiCallListener {
    void startLoading(String requestId);
    void endLoading(String requestId);
}
