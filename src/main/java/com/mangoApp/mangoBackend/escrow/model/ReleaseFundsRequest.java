package com.mangoApp.mangoBackend.escrow.model;

//Petición que hará la app del Transportista para liberar los fondos
public record ReleaseFundsRequest(
 Long orderId,
 String releasePin
) {}