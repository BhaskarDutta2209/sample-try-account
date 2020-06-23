package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import com.r3.corda.lib.accounts.workflows.UtilitiesKt;
import com.r3.corda.lib.accounts.workflows.services.AccountService;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.utilities.ProgressTracker;

import java.util.ArrayList;
import java.util.List;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class GetAccounts extends FlowLogic<List<AccountInfo>> {

    @Suspendable
    @Override
    public List<AccountInfo> call() throws FlowException {
        // Initiator flow logic goes here.
        AccountService accountService = UtilitiesKt.getAccountService(this);

        List<StateAndRef<AccountInfo>> stateAndRefs = accountService.allAccounts();

        List<AccountInfo> accountInfoList = new ArrayList<>();

        for(StateAndRef<AccountInfo> stateAndRef : stateAndRefs) {
            accountInfoList.add(stateAndRef.getState().getData());
        }

        return accountInfoList;
    }
}
