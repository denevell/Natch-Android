package org.denevell.droidnatch.app.interfaces;

public interface OnLongPressObserver<T> {
    
   public interface OnLongPress<T> {
       public void onLongPress(T obj);
   }
    
   public void addOnLongClickListener(OnLongPress<T> callback); 

}
