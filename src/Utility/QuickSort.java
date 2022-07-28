package Utility;

import Variables.Polygon;

import java.util.ArrayList;

public class QuickSort {
    public static ArrayList<Polygon> quickSortPolygons(ArrayList<Polygon> list)
    {
        if (list.isEmpty())
            return list;
        ArrayList<Polygon> sorted;
        ArrayList<Polygon> smaller = new ArrayList<Polygon>();
        ArrayList<Polygon> greater = new ArrayList<Polygon>();
        Polygon pivot = list.get(0);
        int i;
        Polygon j;
        for (i=1;i<list.size();i++)
        {
            j=list.get(i);
            if (j.zDepth > pivot.zDepth)
                smaller.add(j);
            else
                greater.add(j);
        }
        smaller= quickSortPolygons(smaller);
        greater= quickSortPolygons(greater);
        smaller.add(pivot);
        smaller.addAll(greater);
        sorted = smaller;

        return sorted;
    }
}
