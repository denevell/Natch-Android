package org.denevell.droidnatch.app.interfaces;

public interface OnLongPressObserver<T> {
    
   public interface OnLongPress<T> {
       public void onLongPress(T obj, int itemId, String optionName);
   }
    
   public void addOnLongClickListener(OnLongPress<T> callback); 

}
