package school.sptech.prova_ac1;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private  final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {this.usuarioRepository = usuarioRepository;}

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody  Usuario usuario) {
        boolean buscarCpfExistente = usuarioRepository.existsUsuarioByCpf(usuario.getCpf());
        boolean buscarEmailExistente = usuarioRepository.existsUsuarioByEmail(usuario.getEmail());

        if (buscarCpfExistente || buscarEmailExistente) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.status(201).body(usuarioRepository.save(usuario));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
       return usuarioRepository
               .findById(id)
               .map(resp -> ResponseEntity.ok(resp))
               .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        boolean existeUsuario = usuarioRepository.existsById(id);
        if (!existeUsuario) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam  LocalDate nascimento) {
        List<Usuario> usuarios = usuarioRepository.findByDataNascimentoAfter(nascimento);

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(usuarios);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Optional<Usuario> optionalUsuarioExistente = usuarioRepository.findById(id);
        if (!optionalUsuarioExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuarioExistente = optionalUsuarioExistente.get();

        Optional<Usuario> optionalUsuarioComEmail = usuarioRepository.findByEmail(usuario.getEmail());
        if (optionalUsuarioComEmail.isPresent() && !optionalUsuarioComEmail.get().getId().equals(id)) {
            return ResponseEntity.status(409).build();
        }

        Optional<Usuario> optionalUsuarioComCpf = usuarioRepository.findByCpf(usuario.getCpf());
        if (optionalUsuarioComCpf.isPresent() && !optionalUsuarioComCpf.get().getId().equals(id)) {
            return ResponseEntity.status(409).build();
        }



        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setCpf(usuario.getCpf());
        usuarioExistente.setSenha(usuario.getSenha());
        usuarioExistente.setDataNascimento(usuario.getDataNascimento());

        Usuario atualizado = usuarioRepository.saveAndFlush(usuarioExistente);
        return ResponseEntity.ok(atualizado);
    }




}