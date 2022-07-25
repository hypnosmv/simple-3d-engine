package Utility;

import Variables.Face;

import java.util.ArrayList;

public class QuickSort {
    public static ArrayList<Face> quickSortFace(ArrayList<Face> list)
    {
        if (list.isEmpty())
            return list;
        ArrayList<Face> sorted;
        ArrayList<Face> smaller = new ArrayList<Face>();
        ArrayList<Face> greater = new ArrayList<Face>();
        Face pivot = list.get(0);
        int i;
        Face j;
        for (i=1;i<list.size();i++)
        {
            j=list.get(i);
            if (j.zDepth > pivot.zDepth)
                smaller.add(j);
            else
                greater.add(j);
        }
        smaller= quickSortFace(smaller);
        greater= quickSortFace(greater);
        smaller.add(pivot);
        smaller.addAll(greater);
        sorted = smaller;

        return sorted;
    }
}
