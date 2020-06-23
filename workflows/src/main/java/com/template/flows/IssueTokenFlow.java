package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import com.r3.corda.lib.accounts.workflows.UtilitiesKt;
import com.r3.corda.lib.accounts.workflows.flows.RequestKeyForAccount;
import com.r3.corda.lib.tokens.contracts.states.FungibleToken;
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType;
import com.r3.corda.lib.tokens.contracts.types.TokenType;
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens;
import net.corda.core.contracts.Amount;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.AnonymousParty;
import net.corda.core.transactions.SignedTransaction;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Currency;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class IssueTokenFlow extends FlowLogic<String> {

    private final String accountName;
    private final String tokenName;
    private final Long amount;

    public IssueTokenFlow(String accountName, String tokenName, Long amount) {
        this.accountName = accountName;
        this.tokenName = tokenName;
        this.amount = amount;
    }

    public TokenType getInstance(String tokenCode) {
        Currency currency = Currency.getInstance(tokenCode);
        return new TokenType(currency.getCurrencyCode(),0);
    }

    @Suspendable
    @Override
    public String call() throws FlowException {
        // Initiator flow logic goes here.

        AccountInfo accountInfo = UtilitiesKt.getAccountService(this).accountInfo(accountName).get(0).getState().getData();

        AnonymousParty anonymousParty = subFlow(new RequestKeyForAccount(accountInfo));

        TokenType token = getInstance(tokenName);

        IssuedTokenType issuedTokenType = new IssuedTokenType(getOurIdentity(),token);

        FungibleToken fungibleToken = new FungibleToken(new Amount(this.amount, issuedTokenType),anonymousParty,null);

        SignedTransaction stx = subFlow(new IssueTokens(Arrays.asList(fungibleToken)));

        return "Issued "+amount+" "+tokenName+" token(s) to "+accountName+" txId: "+stx.getId().toString();
    }
}
