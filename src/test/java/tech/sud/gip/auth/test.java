package tech.sud.gip.auth;

import tech.sud.gip.auth.model.CodeResponse;
import tech.sud.gip.auth.model.UidResponse;

public class test {
    public static void main(String[] args) {


        SudGIPAuth auth = new SudGIPAuth("1960508867237351424", "vqsfzszmxj6i95s0b9arspynzxwt4fac");

        CodeResponse codeResponse = auth.getCode("672");
        System.out.println(codeResponse);
        System.out.println("-----------------------");

        UidResponse uidResponse = auth.getUidByCode(codeResponse.getCode());

        System.out.printf("uidResponse=%s\n", uidResponse);
    }
}
