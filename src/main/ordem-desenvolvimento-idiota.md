# Ordem de Desenvolvimento de um Novo Recurso ΓÇö Explicado para Idiotas

Objetivo: explicar cada etapa de forma bem simples, mostrando o que voc├¬ faz em cada passo e por que precisa fazer nessa ordem espec├¡fica.

---

## ≡ƒÄ» Cen├írio pr├ítico

Voc├¬ quer criar um novo recurso no sistema chamado **"Calculation"** (c├ílculos de RV). Vamos imaginar que:
- Um usu├írio cria um c├ílculo via API
- O c├ílculo ├⌐ salvo no banco de dados
- O usu├írio pode listar, atualizar e deletar c├ílculos

Essa ├⌐ uma funcionalidade completa que precisa de v├írios arquivos. A ordem que vou descrever te diz **por onde come├ºar e por que**.

---

## ≡ƒôï Ordem de desenvolvimento (11 passos)

### PASSO 1: Definir os DTOs (Data Transfer Objects) ΓÇö O "Formul├írio" da sua API

**O que voc├¬ faz aqui:**
- Voc├¬ cria classes que representam os dados que entram e saem da sua API.
- ├ë como se fossem "formul├írios": um formul├írio para criar, um para atualizar, um para responder.

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/core/port/in/dto/request/calculations/CalculationCreateRequestDto.java` ΓÇö quando o usu├írio CRIA um c├ílculo
- `src/main/java/br/com/tlf/clcs/core/port/in/dto/request/calculations/CalculationUpdateRequestDto.java` ΓÇö quando o usu├írio ATUALIZA um c├ílculo
- `src/main/java/br/com/tlf/clcs/core/port/in/dto/response/calculations/CalculationResponseDto.java` ΓÇö quando voc├¬ RETORNA um c├ílculo

**Exemplo:**
```java
// CalculationCreateRequestDto ΓÇö o que o usu├írio manda para voc├¬ criar
@Data
public class CalculationCreateRequestDto {
    private String name;
    private BigDecimal value;
    private Integer segmentId;
}

// CalculationResponseDto ΓÇö o que voc├¬ retorna pro usu├írio
@Data
public class CalculationResponseDto {
    private Integer calculationId;
    private String name;
    private BigDecimal value;
    private LocalDateTime createdAt;
}
```

**Por que ├⌐ o primeiro passo:**
- Voc├¬ precisa saber o que vai receber e o que vai dar de volta ANTES de fazer qualquer outra coisa.
- ├ë tipo escrever a ementa do card├ípio antes de cozinhar ΓÇö se voc├¬ mudar depois, tudo fica bagun├ºado.
- O frontend precisa saber qual formato de dados mandar ΓÇö isso ├⌐ um contrato entre voc├¬ e ele.

**Como validar:**
- Compile o c├│digo: `.\mvnw.cmd -q compile`
- Se compilar sem erros, essa etapa est├í OK.

---

### PASSO 2: Criar a interface de entrada (PortIn) ΓÇö O "Contrato" do seu servi├ºo

**O que voc├¬ faz aqui:**
- Voc├¬ cria uma interface (tipo um "checklist") que diz o que o seu servi├ºo vai fazer.
- ├ë como escrever uma lista de promessas: "Vou ter um m├⌐todo para criar, um para listar, um para deletar, etc."

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/core/port/in/CalculationPortIn.java`

**Exemplo:**
```java
public interface CalculationPortIn {
    
    // Cria um c├ílculo novo
    CalculationResponseDto create(CalculationCreateRequestDto request);
    
    // Busca um c├ílculo pelo ID
    CalculationResponseDto findById(Integer id);
    
    // Lista todos com pagina├º├úo
    PageResponseDto<CalculationResponseDto> findAll(int page, int size);
    
    // Atualiza um c├ílculo
    CalculationResponseDto update(Integer id, CalculationUpdateRequestDto request);
    
    // Deleta um c├ílculo
    void delete(Integer id);
}
```

**Por que ├⌐ o segundo passo:**
- A interface diz **o que voc├¬ vai fazer**, mas n├úo **como voc├¬ vai fazer**.
- Seu Controller vai conversar com essa interface, n├úo diretamente com o servi├ºo.
- Se voc├¬ mudar a implementa├º├úo depois, o Controller continua funcionando (porque ele s├│ fala com a interface).

**Como validar:**
- Compile: `.\mvnw.cmd -q compile`
- A interface s├│ tem nomes de m├⌐todos e tipos de retorno ΓÇö sem l├│gica ainda.

---

### PASSO 3: Criar a entidade de dom├¡nio ΓÇö A "Verdade" do seu recurso

**O que voc├¬ faz aqui:**
- Voc├¬ cria a classe que representa o seu recurso **no mundo real**, sem pensar em banco de dados.
- ├ë a classe Java pura: dados + comportamento + regras.

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/core/domain/entity/calculations/Calculation.java`

**Exemplo:**
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calculation {
    private Integer calculationId;
    private String name;
    private BigDecimal value;
    private Integer segmentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    
    // Voc├¬ pode adicionar regras aqui
    public boolean isRecent() {
        return ChronoUnit.DAYS.between(createdAt, LocalDateTime.now()) <= 7;
    }
}
```

**Por que ├⌐ o terceiro passo:**
- Essa classe ├⌐ a "verdade" do seu neg├│cio ΓÇö ela n├úo sabe nada de banco de dados.
- O Servi├ºo vai usar essa classe para trabalhar.
- Se voc├¬ mudar o banco de dados depois (trocar PostgreSQL por MySQL), essa classe continua igual.
- Sem anota├º├╡es JPA (`@Entity`, `@Column`) ΓÇö s├│ Lombok mesmo.

**Como validar:**
- Compile: `.\mvnw.cmd -q compile`
- Crie uma inst├óncia em um teste simples para ver se tudo funciona.

---

### PASSO 4: Definir a porta de sa├¡da (PortOut) ΓÇö O "Contrato" da persist├¬ncia

**O que voc├¬ faz aqui:**
- Voc├¬ cria uma interface que diz como voc├¬ quer falar com o banco de dados.
- ├ë tipo escrever um checklist: "Vou ter um m├⌐todo para salvar, um para buscar por ID, etc."

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/core/port/out/CalculationRepositoryPortOut.java`

**Exemplo:**
```java
public interface CalculationRepositoryPortOut {
    Calculation save(Calculation entity);
    Optional<Calculation> findById(Integer id);
    Page<Calculation> findAllPaged(int page, int size);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
```

**Por que ├⌐ o quarto passo:**
- O Servi├ºo vai **depender dessa interface**, n├úo do banco direto.
- Voc├¬ define aqui o que o banco precisa fazer, sem dizer **como** fazer.
- Depois, o Persistence vai implementar essa interface.

**Analogia:**
- Voc├¬ (o Servi├ºo) quer um caf├⌐. Voc├¬ n├úo precisa saber como o caf├⌐ ├⌐ feito ΓÇö voc├¬ s├│ fala: "me traz um caf├⌐". A interface PortOut ├⌐ tipo isso ΓÇö voc├¬ s├│ define o que quer.

**Como validar:**
- Compile: `.\mvnw.cmd -q compile`
- Nessa etapa ainda n├úo h├í implementa├º├úo, s├│ a interface.

---

### PASSO 5: Criar a entidade JPA e o JpaRepository ΓÇö A "Conversa" com o banco

**O que voc├¬ faz aqui:**
- Voc├¬ cria uma classe que representa como o seu recurso ├⌐ guardado **no banco de dados**.
- E um "reposit├│rio" que sabe como conversar com o banco (buscar, salvar, deletar).

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/infrastructure/persistence/model/CalculationEntity.java`
- `src/main/java/br/com/tlf/clcs/infrastructure/persistence/postgres/calculations/CalculationRepository.java`

**Exemplo da Entity:**
```java
@Entity
@Table(name = "tb_calculations", schema = "rv")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculation_id")
    private Integer calculationId;
    
    @Column(name = "name", length = 200)
    private String name;
    
    @Column(name = "value")
    private BigDecimal value;
    
    @Column(name = "segment_id")
    private Integer segmentId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "is_active")
    private Boolean isActive;
}
```

**Exemplo do Repository:**
```java
@Repository
public interface CalculationRepository extends JpaRepository<CalculationEntity, Integer> {
    List<CalculationEntity> findBySegmentId(Integer segmentId);
}
```

**Por que ├⌐ o quinto passo:**
- Agora voc├¬ est├í dizendo ao Spring como falar com o banco de dados.
- O `JpaRepository` j├í tem m├⌐todos prontos: `save()`, `findById()`, `delete()`, etc.
- Voc├¬ s├│ precisa de anota├º├╡es `@Entity`, `@Column`, `@Id` para o Spring entender.

**Importante:**
- A `CalculationEntity` pode ter campos **diferentes** da `Calculation` dom├¡nio ΓÇö isso ├⌐ normal!
- Por exemplo, a entity pode ter `created_at` (camelCase) e a dom├¡nio pode usar `createdAt` ΓÇö o `ModelMapper` converte.

**Como validar:**
- Compile: `.\mvnw.cmd -q compile`
- Depois, quando tiver banco em p├⌐, testar se as queries rodam.

---

### PASSO 6: Implementar o Adapter de Persist├¬ncia (Persistence) ΓÇö O "Tradutor" banco Γåö dom├¡nio

**O que voc├¬ faz aqui:**
- Voc├¬ cria uma classe que **implementa a PortOut** (passo 4).
- Essa classe sabe como converter dados entre `Calculation` (dom├¡nio) Γåö `CalculationEntity` (JPA).
- ├ë tipo um tradutor: quando algu├⌐m pede um Calculation, voc├¬ traduz da entity.

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/infrastructure/persistence/postgres/calculations/CalculationPersistence.java`

**Exemplo (bem parecido com BasesPersistence que voc├¬ passou):**
```java
@Component
public class CalculationPersistence implements CalculationRepositoryPortOut {

    private final CalculationRepository repository;
    private final ModelMapper mapper;

    public CalculationPersistence(CalculationRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Calculation save(Calculation entity) {
        // Converte Calculation (dom├¡nio) para CalculationEntity (JPA)
        CalculationEntity jpaEntity = mapper.map(entity, CalculationEntity.class);
        
        // Salva no banco
        CalculationEntity saved = repository.save(jpaEntity);
        
        // Converte de volta para Calculation (dom├¡nio)
        return mapper.map(saved, Calculation.class);
    }

    @Override
    public Optional<Calculation> findById(Integer id) {
        return repository.findById(id)
                .map(jpaEntity -> mapper.map(jpaEntity, Calculation.class));
    }

    @Override
    public Page<Calculation> findAllPaged(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findAll(pageRequest)
                .map(entity -> mapper.map(entity, Calculation.class));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
```

**Por que ├⌐ o sexto passo:**
- Voc├¬ s├│ implementa o PortOut **depois** de ter criado a Entity JPA.
- Agora voc├¬ sabe como falar com o banco (via `repository`).
- O `ModelMapper` faz as convers├╡es autom├íticas (se os nomes baterem).

**Analogia:**
- O Service vai pedir coisas para a PortOut: "me traz um c├ílculo".
- O Persistence ouve e faz: busca no banco (via repository), converte para dom├¡nio, e entrega.

**Como validar:**
- Compile: `.\mvnw.cmd -q compile`
- Se tiver banco rodando, rodar um teste simples: criar um Calculation via persistence e verificar se apareceu no banco.

---

### PASSO 7: Implementar o Service (implementa PortIn) ΓÇö A "L├│gica de neg├│cio"

**O que voc├¬ faz aqui:**
- Voc├¬ cria uma classe `@Service` que **implementa a PortIn** (passo 2).
- Essa classe cont├⌐m a l├│gica de neg├│cio: valida├º├╡es, convers├╡es, orquestra├º├úo.
- ├ë tipo um gerente: recebe pedidos (do controller), fala com o banco (via persistence) e entrega resultados.

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/core/application/service/calculations/CalculationService.java`

**Exemplo:**
```java
@Service
@RequiredArgsConstructor
@Transactional
public class CalculationService implements CalculationPortIn {

    private final CalculationRepositoryPortOut repositoryPortOut;
    private final ModelMapper mapper;

    @Override
    public CalculationResponseDto create(CalculationCreateRequestDto request) {
        // Converte DTO de entrada para dom├¡nio
        Calculation calculation = mapper.map(request, Calculation.class);
        
        // Define dados autom├íticos
        calculation.setCreatedAt(LocalDateTime.now());
        calculation.setIsActive(true);
        
        // Salva via porta de sa├¡da (que chama o persistence)
        Calculation saved = repositoryPortOut.save(calculation);
        
        // Converte dom├¡nio para DTO de resposta
        return mapper.map(saved, CalculationResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CalculationResponseDto findById(Integer id) {
        Calculation calculation = repositoryPortOut.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("C├ílculo n├úo encontrado: " + id));
        return mapper.map(calculation, CalculationResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<CalculationResponseDto> findAll(int page, int size) {
        Page<Calculation> resultPage = repositoryPortOut.findAllPaged(page, size);
        
        var pageResponse = mapper.map(resultPage, PageResponseDto.class);
        
        List<CalculationResponseDto> content = resultPage.getContent()
                .stream()
                .map(calc -> mapper.map(calc, CalculationResponseDto.class))
                .collect(Collectors.toList());
        
        pageResponse.setContent(content);
        return pageResponse;
    }

    @Override
    public CalculationResponseDto update(Integer id, CalculationUpdateRequestDto request) {
        Calculation existing = repositoryPortOut.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("C├ílculo n├úo encontrado: " + id));
        
        // Map da request para a entidade existente
        mapper.map(request, existing);
        
        // Atualiza timestamp
        existing.setUpdatedAt(LocalDateTime.now());
        
        // Salva
        Calculation updated = repositoryPortOut.save(existing);
        return mapper.map(updated, CalculationResponseDto.class);
    }

    @Override
    public void delete(Integer id) {
        if (!repositoryPortOut.existsById(id)) {
            throw new EntityNotFoundException("C├ílculo n├úo encontrado: " + id);
        }
        repositoryPortOut.deleteById(id);
    }
}
```

**Por que ├⌐ o s├⌐timo passo:**
- O Service **depende de PortIn e PortOut**, ent├úo voc├¬ cria depois de ter definido essas interfaces.
- Aqui ├⌐ onde voc├¬ adiciona l├│gica: timestamps autom├íticos, valida├º├╡es, convers├╡es de DTO Γåö dom├¡nio.
- O Service **n├úo sabe** como o banco funciona ΓÇö ele s├│ fala com a PortOut (que ├⌐ implementada pelo Persistence).

**Analogia:**
- O Service ├⌐ tipo um gerente de loja: ele recebe clientes (controllers), tira as coisas da prateleira (persistence), embrulha (mapeia) e entrega.

**Como validar:**
- Teste unit├írio que mocka `CalculationRepositoryPortOut` e testa se o `create()` funciona.

---

### PASSO 8: Criar a interface do Controller (contrato REST) ΓÇö O "Card├ípio" da sua API

**O que voc├¬ faz aqui:**
- Voc├¬ cria uma interface que define como a sua API funciona por fora.
- ├ë tipo escrever um card├ípio: "POST /calculations cria, GET /calculations/{id} busca, DELETE /calculations/{id} deleta".
- Voc├¬ coloca anota├º├╡es OpenAPI aqui para documenta├º├úo autom├ítica.

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/api/rest/calculations/CalculationController.java`

**Exemplo:**
```java
@Tag(name = "Calculation API", description = "REST API para gerenciar c├ílculos")
public interface CalculationController {

    @Operation(summary = "Busca um c├ílculo pelo ID")
    @ApiResponse(responseCode = "200", description = "C├ílculo encontrado")
    @ApiResponse(responseCode = "404", description = "C├ílculo n├úo encontrado")
    @GetMapping("/{id}")
    ResponseEntity<CalculationResponseDto> getById(@PathVariable Integer id);

    @Operation(summary = "Lista todos os c├ílculos com pagina├º├úo")
    @GetMapping
    ResponseEntity<PageResponseDto<CalculationResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "Cria um novo c├ílculo")
    @ApiResponse(responseCode = "201", description = "C├ílculo criado")
    @PostMapping
    ResponseEntity<CalculationResponseDto> create(
            @RequestBody @Valid CalculationCreateRequestDto request);

    @Operation(summary = "Atualiza um c├ílculo")
    @PutMapping("/{id}")
    ResponseEntity<CalculationResponseDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid CalculationUpdateRequestDto request);

    @Operation(summary = "Deleta um c├ílculo")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id);
}
```

**Por que ├⌐ o oitavo passo:**
- A interface define o contrato REST: URLs, m├⌐todos HTTP, o que entra, o que sai.
- Voc├¬ coloca anota├º├╡es OpenAPI (`@Operation`, `@ApiResponse`) aqui.
- O frontend consegue ler essas anota├º├╡es e gerar documenta├º├úo autom├ítica.

**Como validar:**
- Compile: `.\mvnw.cmd -q compile`
- Se tiver OpenAPI configurado, v├¬ se aparece em `/swagger-ui.html`.

---

### PASSO 9: Implementar o Controller (impl) ΓÇö A "Orquestra├º├úo" final

**O que voc├¬ faz aqui:**
- Voc├¬ cria uma classe `@RestController` que **implementa a interface** (passo 8).
- Essa classe recebe requisi├º├╡es HTTP, chama o Service e monta a resposta.
- ├ë a "porta de entrada" da sua aplica├º├úo.

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/api/rest/calculations/CalculationControllerImpl.java`

**Exemplo:**
```java
@RestController
@RequestMapping("/calculations")
@RequiredArgsConstructor
@Slf4j
public class CalculationControllerImpl implements CalculationController {

    private final CalculationPortIn calculationPortIn;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CalculationResponseDto> getById(@PathVariable Integer id) {
        log.info("Buscando c├ílculo com ID: {}", id);
        CalculationResponseDto response = calculationPortIn.findById(id);
        response = HateoasResponseBuilder.addLinksHateoas(response);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponseDto<CalculationResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Listando c├ílculos - page: {}, size: {}", page, size);
        PageResponseDto<CalculationResponseDto> response = calculationPortIn.findAll(page, size);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping
    public ResponseEntity<CalculationResponseDto> create(
            @RequestBody @Valid CalculationCreateRequestDto request) {
        log.info("Criando novo c├ílculo");
        CalculationResponseDto response = calculationPortIn.create(request);

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getCalculationId())
                .toUri();

        response = HateoasResponseBuilder.addLinksHateoas(response);
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CalculationResponseDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid CalculationUpdateRequestDto request) {
        log.info("Atualizando c├ílculo com ID: {}", id);
        CalculationResponseDto response = calculationPortIn.update(id, request);
        response = HateoasResponseBuilder.addLinksHateoas(response);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Deletando c├ílculo com ID: {}", id);
        calculationPortIn.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

**Por que ├⌐ o nono passo:**
- O Controller **depende de PortIn** (que ├⌐ implementada pelo Service).
- Agora voc├¬ consegue receber requisi├º├╡es HTTP e chamar o Service.
- O Controller n├úo tem l├│gica de neg├│cio ΓÇö s├│ orquestra: recebe ΓåÆ chama service ΓåÆ devolve.

**Analogia:**
- O Controller ├⌐ tipo um atendente de loja: recebe o cliente (requisi├º├úo HTTP), vai falar com o gerente (Service) e entrega o resultado.

**Como validar:**
- Teste controller com MockMvc que mocka `CalculationPortIn`.
- Ou rodar a app e fazer um curl.

---

### PASSO 10: Configura├º├╡es transversais (ModelMapper, DB, etc.) ΓÇö O "Suporte"

**O que voc├¬ faz aqui:**
- Voc├¬ adiciona configura├º├╡es que outros componentes precisam.
- Por exemplo, configurar `ModelMapper`, banco de dados, CORS, seguran├ºa, etc.

**Onde voc├¬ coloca:**
- `src/main/java/br/com/tlf/clcs/shared/config/ModelMapperConfig.java` (j├í existe no projeto)
- `src/main/resources/application.yml` ΓÇö arquivo de configura├º├úo

**Exemplo (ModelMapperConfig):**
```java
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSkipNullEnabled(true);
        return mapper;
    }
}
```

**Por que ├⌐ o d├⌐cimo passo:**
- Voc├¬ cria as configs **depois** de ter a maioria dos componentes, porque algumas configs depende deles.
- Por exemplo, `ModelMapper` s├│ faz sentido depois que voc├¬ tem DTOs e entidades.

**Como validar:**
- Compile: `.\mvnw.cmd -q compile`
- Se rodar a app, verificar se `ModelMapper` est├í sendo injetado.

---

### PASSO 11: Testes e Migrations DB ΓÇö A "Valida├º├úo"

**O que voc├¬ faz aqui:**
- Voc├¬ testa se tudo funciona (testes unit├írios, testes de integra├º├úo).
- Voc├¬ cria scripts para criar as tabelas no banco de dados.

**Onde voc├¬ coloca:**
- Testes: `src/test/java/br/com/tlf/clcs/core/application/service/calculations/CalculationServiceTest.java`
- Migrations: `src/main/resources/db/migration/V1__create_calculations_table.sql`

**Exemplo de teste:**
```java
@ExtendWith(MockitoExtension.class)
public class CalculationServiceTest {

    @Mock
    private CalculationRepositoryPortOut repositoryPortOut;

    @Mock
    private ModelMapper modelMapper;

    private CalculationService service;

    @BeforeEach
    void setUp() {
        service = new CalculationService(repositoryPortOut, modelMapper);
    }

    @Test
    void testCreate() {
        // Arrange
        CalculationCreateRequestDto request = new CalculationCreateRequestDto();
        request.setName("Test Calc");
        
        Calculation domainCalc = new Calculation();
        domainCalc.setCalculationId(1);
        
        when(repositoryPortOut.save(any())).thenReturn(domainCalc);
        when(modelMapper.map(any(), any())).thenReturn(domainCalc);
        
        // Act
        CalculationResponseDto result = service.create(request);
        
        // Assert
        assertNotNull(result);
        verify(repositoryPortOut, times(1)).save(any());
    }
}
```

**Exemplo de migration:**
```sql
CREATE TABLE rv.tb_calculations (
    calculation_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    value DECIMAL(19, 4),
    segment_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);
```

**Por que ├⌐ o d├⌐cimo primeiro passo:**
- Voc├¬ testa **depois** que tudo est├í implementado.
- Testes validam se o c├│digo funciona como esperado.
- Migrations garantem que o banco tem a tabela certa.

**Como validar:**
- Rodar testes: `.\mvnw.cmd -q test`
- Rodar migrations (Flyway faz autom├ítico ao iniciar a app).

---

## ≡ƒöù Como tudo se encaixa (resumo visual)

```
1. DTOs (formul├írios)
        Γåô
2. PortIn (promessas do Service)
        Γåô
3. Entidade de Dom├¡nio (verdade do neg├│cio)
        Γåô
4. PortOut (promessas da Persist├¬ncia)
        Γåô
5. Entity JPA + Repository (conversa com banco)
        Γåô
6. Persistence (implementa PortOut ΓÇö tradutor banco Γåö dom├¡nio)
        Γåô
7. Service (implementa PortIn ΓÇö l├│gica de neg├│cio)
        Γåô
8. Controller Interface (contrato REST)
        Γåô
9. Controller Impl (orquestra├º├úo final)
        Γåô
10. Configs transversais (ModelMapper, DB)
        Γåô
11. Testes + Migrations (valida├º├úo)
```

---

## ≡ƒôè Fluxo de uma requisi├º├úo (como tudo funciona junto)

Vamos rastrear uma requisi├º├úo: **POST /calculations com dados para criar um novo c├ílculo**

```
1. Usu├írio faz requisi├º├úo HTTP
   POST /calculations
   Body: { "name": "Calc A", "value": 100 }
        Γåô
2. CalculationControllerImpl.create() recebe a requisi├º├úo
   - Valida o @RequestBody (DTOs t├¬m @Valid)
   - Chama calculationPortIn.create(request)
        Γåô
3. CalculationService.create() (implementa├º├úo de PortIn) executa
   - Map CalculationCreateRequestDto ΓåÆ Calculation (dom├¡nio)
   - Seta createdAt = now(), isActive = true
   - Chama repositoryPortOut.save(calculation)
        Γåô
4. CalculationPersistence.save() (implementa├º├úo de PortOut) executa
   - Map Calculation ΓåÆ CalculationEntity (JPA)
   - Chama repository.save(jpaEntity) [Spring Data]
   - Map CalculationEntity ΓåÆ Calculation (dom├¡nio) de volta
   - Retorna para o Service
        Γåô
5. CalculationRepository.save() (Spring Data JPA) executa
   - INSERT na tabela tb_calculations
   - Retorna a entity com ID preenchido
        Γåô
6. Service retorna CalculationResponseDto (convertido via mapper)
        Γåô
7. ControllerImpl adiciona HATEOAS e monta ResponseEntity
   - Status 201 Created
   - Header Location: /calculations/123
   - Body: { "calculationId": 123, "name": "Calc A", ... }
        Γåô
8. Resposta ├⌐ devolvida ao usu├írio
```

---

## ≡ƒÄ» Resumo: Por que essa ordem?

| Passo | Por qu├¬ |
|-------|--------|
| 1. DTOs | Contrato com o mundo externo ΓÇö precisa existir primeiro |
| 2. PortIn | Define o que o Service vai fazer |
| 3. Entidade Dom├¡nio | Representa a verdade do neg├│cio |
| 4. PortOut | Define como o Persistence vai trabalhar |
| 5. Entity JPA + Repo | Sabe como conversar com o banco |
| 6. Persistence | Implementa PortOut ΓÇö traduz banco Γåö dom├¡nio |
| 7. Service | Implementa PortIn ΓÇö l├│gica + orquestra |
| 8. Controller Interface | Define contrato REST |
| 9. Controller Impl | Implementa a orquestra├º├úo final |
| 10. Configs | Suporte compartilhado (ModelMapper, etc.) |
| 11. Testes + Migrations | Valida e cria estrutura no DB |

---

## Γ£à Checklist para criar um novo recurso

Use esse checklist toda vez que criar um novo recurso:

- [ ] 1. Criar DTOs (CreateDto, UpdateDto, ResponseDto)
- [ ] 2. Criar PortIn (interface com m├⌐todos CRUD)
- [ ] 3. Criar Entidade de Dom├¡nio
- [ ] 4. Criar PortOut (interface de persist├¬ncia)
- [ ] 5. Criar Entity JPA e JpaRepository
- [ ] 6. Implementar Persistence (implementa PortOut)
- [ ] 7. Implementar Service (implementa PortIn)
- [ ] 8. Criar Controller Interface (com OpenAPI)
- [ ] 9. Implementar ControllerImpl (implementa interface)
- [ ] 10. Adicionar configs (se necess├írio)
- [ ] 11. Escrever testes unit├írios
- [ ] 12. Criar migration SQL
- [ ] 13. Testar: `.\mvnw.cmd -q test`
- [ ] 14. Testar app: `.\mvnw.cmd spring-boot:run` + curl

---

## ≡ƒô¥ Nota importante: BasesPersistence como refer├¬ncia

O arquivo que voc├¬ passou (`BasesPersistence.java`) ├⌐ um exemplo de implementa├º├úo do passo 6:

```java
@Component  // ΓåÉ Spring registra como bean
public class BasesPersistence implements BasesRepositoryPortOut {  // ΓåÉ implementa PortOut

    private final BasesRepository repository;  // ΓåÉ injeta JpaRepository
    private final ModelMapper mapper;  // ΓåÉ injeta ModelMapper

    @Override
    public Page<Bases> findByDescriptionPaged(String description, int page, int size) {
        // Aqui: constr├│i Specification, chama repository, mapeia resultado
        // ├ë o tradutor banco Γåö dom├¡nio
    }

    @Override
    public Bases save(Bases entity) {
        // Map Bases (dom├¡nio) ΓåÆ BasesEntity (JPA)
        BasesEntity jpaEntity = mapper.map(entity, BasesEntity.class);
        
        // Salva no banco
        BasesEntity saved = repository.save(jpaEntity);
        
        // Map de volta para dom├¡nio
        return mapper.map(saved, Bases.class);
    }
}
```

Esse padr├úo repete-se para **todos os recursos**: Request, Segment, Channel, Person, etc. ΓÇö eles todos seguem essa arquitetura.

---

Fim. Agora voc├¬ sabe por que cada coisa ├⌐ feita nessa ordem e como elas se conectam. ≡ƒÄë

