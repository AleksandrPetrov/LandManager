package com.land.shared.dto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ObjectAggregatedExpenseItemDto implements IDto {

    private ObjectDto landObject = null;

    private List<AggregatedExpensesDto> aggregatedExpenseList = new ArrayList<AggregatedExpensesDto>();

    public ObjectAggregatedExpenseItemDto() {
        super();
    }

    public ObjectDto getLandObject() {
        return landObject;
    }

    public void setLandObject(ObjectDto landObject) {
        this.landObject = landObject;
    }

    public List<AggregatedExpensesDto> getAggregatedExpenseList() {
        return aggregatedExpenseList;
    }

    public void setAggregatedExpenseList(List<AggregatedExpensesDto> aggregatedExpenseList) {
        this.aggregatedExpenseList = aggregatedExpenseList;
    }

    public ObjectAggregatedExpenseItemDto clone() {
        ObjectAggregatedExpenseItemDto res = new ObjectAggregatedExpenseItemDto();
        res.setLandObject(landObject == null ? null : landObject.clone());
        for (AggregatedExpensesDto agi : aggregatedExpenseList)
            res.getAggregatedExpenseList().add(agi.clone());
        return res;
    }
}
