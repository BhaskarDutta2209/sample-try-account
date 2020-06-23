package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import com.r3.corda.lib.accounts.workflows.flows.CreateAccount;
import com.r3.corda.lib.accounts.workflows.flows.ShareAccountInfo;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.utilities.ProgressTracker;

import java.util.List;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class CreateAndShareAccountFlow extends FlowLogic<String> {

    private final String accountName;
    private final List<Party> partyToShareAccountInfoList;

    private final ProgressTracker progressTracker = new ProgressTracker();

    public CreateAndShareAccountFlow(String accountName, List<Party> partyToShareAccountInfoList) {
        this.accountName = accountName;
        this.partyToShareAccountInfoList = partyToShareAccountInfoList;
    }

    @Suspendable
    @Override
    public String call() throws FlowException {
        // Call inbuilt Create
        StateAndRef<AccountInfo> accountInfoStateAndRef = (StateAndRef<AccountInfo>) subFlow(new CreateAccount(accountName));

        //Share this AccountInfo object with the parties who want to transact with this account
        subFlow(new ShareAccountInfo(accountInfoStateAndRef, partyToShareAccountInfoList));

        return ""+accountName+"has been created and shared to "+partyToShareAccountInfoList+".";
    }
}
