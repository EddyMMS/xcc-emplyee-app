package tech.mms.cos.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class LoginFailedException extends AbstractThrowableProblem {

  public LoginFailedException(String message) {
    super(null, "Login Failed", Status.UNAUTHORIZED, message);
  }
}
