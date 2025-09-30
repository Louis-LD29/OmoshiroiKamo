package louis.omoshiroikamo.client.gui.modularui2;

public interface IModularSortable {

    void addSortArea(String key, int rowSize);

    int getRowSize(String key);
}
