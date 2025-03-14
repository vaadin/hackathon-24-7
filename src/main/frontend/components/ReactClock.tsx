import React, { useEffect, useRef, useState } from 'react';

interface ClockProps {
    size?: number;
}

const ReactClock: React.FC<ClockProps> = ({ size = 300 }) => {
    const [time, setTime] = useState(new Date());
    const canvasRef = useRef<HTMLCanvasElement>(null);

    useEffect(() => {
        const timer = setInterval(() => {
            setTime(new Date());
        }, 1000);

        return () => clearInterval(timer);
    }, []);

    useEffect(() => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext('2d');
        if (!ctx) return;

        // Clear canvas
        ctx.clearRect(0, 0, size, size);

        // Set up clock face
        const centerX = size / 2;
        const centerY = size / 2;
        const radius = (size / 2) * 0.9;

        // Draw clock face
        ctx.beginPath();
        ctx.arc(centerX, centerY, radius, 0, 2 * Math.PI);
        ctx.strokeStyle = 'var(--lumo-primary-color)';
        ctx.lineWidth = 3;
        ctx.stroke();

        // Draw hour markers
        for (let i = 0; i < 12; i++) {
            const angle = (i * Math.PI) / 6 - Math.PI / 2;
            const markerLength = i % 3 === 0 ? 15 : 10;

            const startX = centerX + (radius - markerLength) * Math.cos(angle);
            const startY = centerY + (radius - markerLength) * Math.sin(angle);
            const endX = centerX + radius * Math.cos(angle);
            const endY = centerY + radius * Math.sin(angle);

            ctx.beginPath();
            ctx.moveTo(startX, startY);
            ctx.lineTo(endX, endY);
            ctx.strokeStyle = 'var(--lumo-primary-color)';
            ctx.lineWidth = i % 3 === 0 ? 3 : 2;
            ctx.stroke();
        }

        // Calculate hand angles
        const hours = time.getHours() % 12;
        const minutes = time.getMinutes();
        const seconds = time.getSeconds();

        // Draw hour hand
        const hourAngle = (hours + minutes / 60) * (Math.PI / 6) - Math.PI / 2;
        drawHand(ctx, centerX, centerY, radius * 0.5, hourAngle, 6);

        // Draw minute hand
        const minuteAngle = minutes * (Math.PI / 30) - Math.PI / 2;
        drawHand(ctx, centerX, centerY, radius * 0.7, minuteAngle, 4);

        // Draw second hand
        const secondAngle = seconds * (Math.PI / 30) - Math.PI / 2;
        ctx.strokeStyle = 'var(--lumo-error-color)';
        drawHand(ctx, centerX, centerY, radius * 0.8, secondAngle, 2);

        // Draw center dot
        ctx.beginPath();
        ctx.arc(centerX, centerY, 5, 0, 2 * Math.PI);
        ctx.fillStyle = 'var(--lumo-error-color)';
        ctx.fill();

    }, [time, size]);

    const drawHand = (
        ctx: CanvasRenderingContext2D,
        x: number,
        y: number,
        length: number,
        angle: number,
        width: number
    ) => {
        ctx.beginPath();
        ctx.moveTo(x, y);
        ctx.lineTo(
            x + length * Math.cos(angle),
            y + length * Math.sin(angle)
        );
        ctx.strokeStyle = 'var(--lumo-primary-color)';
        ctx.lineWidth = width;
        ctx.lineCap = 'round';
        ctx.stroke();
    };

    return (
        <canvas
            ref={canvasRef}
            width={size}
            height={size}
            style={{
                width: '100%',
                height: '100%',
                maxWidth: `${size}px`,
                maxHeight: `${size}px`,
                margin: 'auto',
                display: 'block'
            }}
        />
    );
};

export default ReactClock;
