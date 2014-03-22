package org.denevell.droidnatch.app.interfaces;

public interface OnPressObserver<T> {
    
   public interface OnPress<T> {
       public void onPress(T obj);
   }
    
   public void addOnPressListener(OnPress<T> callback); 

}
