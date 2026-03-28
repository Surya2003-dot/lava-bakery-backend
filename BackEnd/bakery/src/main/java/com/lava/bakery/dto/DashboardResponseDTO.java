package com.lava.bakery.dto;

public class DashboardResponseDTO {

    private long totalOrders;
    private long pending;
    private long confirmed;
    private long preparing;
    private long outForDelivery;
    private long delivered;
    private long completed;
    private long cancelled;
    private double totalRevenue;

    public DashboardResponseDTO(long totalOrders,
                                long pending,
                                long confirmed,
                                long preparing,
                                long outForDelivery,
                                long delivered,
                                long completed,
                                long cancelled,
                                double totalRevenue) {

        this.totalOrders = totalOrders;
        this.pending = pending;
        this.confirmed = confirmed;
        this.preparing = preparing;
        this.outForDelivery = outForDelivery;
        this.delivered = delivered;
        this.completed = completed;
        this.cancelled = cancelled;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalOrders() { return totalOrders; }
    public long getPending() { return pending; }
    public long getConfirmed() { return confirmed; }
    public long getPreparing() { return preparing; }
    public long getOutForDelivery() { return outForDelivery; }
    public long getDelivered() { return delivered; }
    public long getCompleted() { return completed; }
    public long getCancelled() { return cancelled; }
    public double getTotalRevenue() { return totalRevenue; }
}