package com.anyi.sparrow.mobileStat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDirectListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<DirectCompanyStatVO> companyStatVOList;

}
