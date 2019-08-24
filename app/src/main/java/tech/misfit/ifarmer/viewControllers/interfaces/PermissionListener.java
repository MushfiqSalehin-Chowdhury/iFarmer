package tech.misfit.ifarmer.viewControllers.interfaces;

public interface PermissionListener {
    void permissionGranted();
    void permissionDenied(int position);
}
