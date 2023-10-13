package ao.tdados.tudolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ao.tdados.tudolist.user.IUserRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            var autorization = request.getHeader("Authorization");
            var authEncoded = autorization.substring("Basic".length()).trim();
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
            var authString = new String(authDecoded);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUsername(username);
            if(user == null) {
                response.sendError(401);
            }else{
            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());            
                if(passwordVerify.verified)
                    response.sendError(401);
                else
                    filterChain.doFilter(request, response);                
            }
    }    
}
