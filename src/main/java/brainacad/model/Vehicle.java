package brainacad.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(name = "load_capacity", nullable = false)
    private double loadCapacity;

    @Column(nullable = false)
    private String condition;

    @Column(nullable = false)
    private boolean available = true;
}
