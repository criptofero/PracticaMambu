package com.sofka.practicaMambu.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sofka.practicaMambu.domain.model.query.MambuQueryFilter;
import com.sofka.practicaMambu.domain.model.query.MambuSortingCriteria;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionFilterInfo {
    private MambuQueryFilter[] filterCriteria;
    private MambuSortingCriteria sortingCriteria;

    public MambuQueryFilter[] getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(MambuQueryFilter[] filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    public MambuSortingCriteria getSortingCriteria() {
        return sortingCriteria;
    }

    public void setSortingCriteria(MambuSortingCriteria sortingCriteria) {
        this.sortingCriteria = sortingCriteria;
    }
}
