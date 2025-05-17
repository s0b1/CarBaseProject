package brainacad.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cargo_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoRequest
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destination;

    @Column(name = "cargo_type", nullable = false)
    private String cargoType;

    @Column(nullable = false)
    private double weight;
}
