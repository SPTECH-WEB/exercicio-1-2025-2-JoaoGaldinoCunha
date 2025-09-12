package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Optional<Usuario> findByEmail(String email);
    boolean existsUsuarioByCpf(String cpf);
    boolean existsUsuarioByEmail(String email);

    List<Usuario> findByDataNascimentoAfter(LocalDate dataNascimento);
    boolean existsUsuarioById(Integer id);

    Optional<Usuario> findByCpf(String cpf);
}

