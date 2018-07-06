package com.suidifu.microservice.model;

import com.suidifu.microservice.entity.SourceDocument;
import com.zufangbao.sun.entity.account.VirtualAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceDocumentVirtualAccountResolver {
    private SourceDocument sourceDocument;
    private VirtualAccount virtualAccount;
}