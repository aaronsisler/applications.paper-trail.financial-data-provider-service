package com.ebsolutions.papertrail.financialdataproviderservice.transaction;

import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("transactions")
public class TransactionController {
  private final TransactionService transactionService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all transactions")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))
          }),
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))})
  public ResponseEntity<?> getAll(@RequestParam(required = false) Integer accountId) {
    List<Transaction> transactions;

    if (accountId == null) {
      transactions = transactionService.getAll();
    } else {
      transactions = transactionService.getAllByAccountId(accountId);
    }

    return !transactions.isEmpty()
        ? ResponseEntity.ok(transactions) :
        ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get transaction")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = User.class))
          }),
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping(value = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> get(@PathVariable @Valid Integer transactionId) {

    Optional<Transaction> transaction = transactionService.get(transactionId);

    return transaction.isPresent()
        ? ResponseEntity.ok(transaction.get()) :
        ResponseEntity.noContent().build();
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create transactions")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))
          })})
  public ResponseEntity<?> post(@Valid @RequestBody List<@Valid Transaction> transactions) {
    return ResponseEntity.ok(transactionService.createAll(transactions));
  }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update transaction")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = User.class))
          })})
  public ResponseEntity<?> put(@RequestBody @Valid Transaction transaction) {
    return ResponseEntity.ok().body(transactionService.update(transaction));
  }

  @Operation(summary = "Delete transaction")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))
  })
  @DeleteMapping(value = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@PathVariable @Valid Integer transactionId) {

    transactionService.delete(transactionId);

    return ResponseEntity.noContent().build();
  }
}
