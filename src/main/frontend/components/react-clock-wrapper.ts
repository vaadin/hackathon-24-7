import { html, LitElement } from 'lit';
import { customElement, property } from 'lit/decorators.js';
import React from 'react';
import { createRoot } from 'react-dom/client';
import ReactClock from './ReactClock';

@customElement('react-clock')
export class ReactClockWrapper extends LitElement {
    private root: any;
    
    @property({ type: Number })
    size = 300;

    protected createRenderRoot() {
        return this;
    }

    protected firstUpdated() {
        const container = document.createElement('div');
        this.appendChild(container);
        this.root = createRoot(container);
        this.updateReactComponent();
    }

    protected updated() {
        this.updateReactComponent();
    }

    private updateReactComponent() {
        if (this.root) {
            this.root.render(React.createElement(ReactClock, { size: this.size }));
        }
    }

    render() {
        return html``;
    }
}
