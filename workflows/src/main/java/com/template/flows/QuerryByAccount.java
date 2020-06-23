package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import com.r3.corda.lib.accounts.workflows.UtilitiesKt;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.node.services.vault.QueryCriteria;

import java.util.Arrays;
import java.util.UUID;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class QuerryByAccount extends FlowLogic<String> {

    private final String whoAmI;

    public QuerryByAccount(String whoAmI) {
        this.whoAmI = whoAmI;
    }

    @Suspendable
    @Override
    public String call() throws FlowException {
        // Initiator flow logic goes here.
        AccountInfo myAccount = UtilitiesKt.getAccountService(this).accountInfo(whoAmI).get(0).getState().getData();
        UUID id = myAccount.getIdentifier().getId();
        QueryCriteria.VaultQueryCriteria criteria = new QueryCriteria.VaultQueryCriteria().withExternalIds(Arrays.asList(id));



        return null;
    }
}
