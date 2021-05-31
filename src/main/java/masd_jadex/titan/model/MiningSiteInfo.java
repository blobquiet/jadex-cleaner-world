package masd_jadex.titan.model;

import jadex.commons.transformation.annotations.IncludeFields;
import jadex.extension.envsupport.math.IVector2;

@IncludeFields
public class MiningSiteInfo
{
    public int id;
    public IVector2 position;
    public int numSlots;
}
